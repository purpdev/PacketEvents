/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ClassMemberInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.MethodInfoList;
import io.github.classgraph.ScanResult;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassStructureTest extends BaseDummyAPITest {

    private static ScanResult SCAN_RESULT;

    @BeforeAll
    public static void setupClassScan() {
        SCAN_RESULT = new ClassGraph()
                .acceptPackages(PacketEvents.class.getPackage().getName())
                .enableAllInfo()
                .scan();
    }

    @AfterAll
    public static void tearDownClassScan() {
        try (ScanResult scan = SCAN_RESULT) { /**/ }
    }

    @Test
    @DisplayName("Test only one class extends MappedEntity")
    public void testAll() {
        ClassInfoList directMappedEntities = SCAN_RESULT.getClassesImplementing(MappedEntity.class)
                .directOnly().filter(c -> !c.isInterface() && !c.hasAnnotation(Deprecated.class));
        assertEquals(1, directMappedEntities.size(),
                () -> "Found more than one direct MappedEntity implementation; expected only AbstractMappedEntity, found: \n"
                        + directMappedEntities.stream().map(ClassInfo::toStringWithSimpleNames).collect(Collectors.joining("\n")));
    }

    @Test
    @DisplayName("Test mapped entity implementations")
    public void testMappedEntityImplementations() {
        List<String> issues = new ArrayList<>();
        for (ClassInfo mappedEntity : SCAN_RESULT.getAllClasses()
                .filter(info -> info.extendsSuperclass(AbstractMappedEntity.class))
                .filter(info -> !info.isAnonymousInnerClass())
                .filter(c -> !c.hasAnnotation(Deprecated.class))) {
            String classPrefix = '[' + mappedEntity.getName() + ']';
            MethodInfoList constructors = mappedEntity.getConstructorInfo();
            // find data constructor
            List<MethodInfo> dataCtors = constructors.stream()
                    .filter(ClassMemberInfo::isPublic)
                    .filter(info -> Arrays.stream(info.getParameterInfo())
                            .anyMatch(param -> param.getTypeDescriptor().toString()
                                    .contains(TypesBuilderData.class.getName())))
                    .collect(Collectors.toList());
            if (dataCtors.isEmpty()) {
                issues.add(classPrefix + " Found no constructor accepting TypesBuilderData in " + mappedEntity.toStringWithSimpleNames());
            }
            for (MethodInfo dataCtor : dataCtors) {
                // ensure constructor is marked as internal
                if (!dataCtor.hasAnnotation(ApiStatus.Internal.class)) {
                    issues.add(classPrefix + " Constructor accepting TypesBuilderData isn't marked as @ApiStatus.Internal: " + dataCtor.toStringWithSimpleNames());
                }
                // ensure TypesBuilderData type is annotated as Nullable
                if (Arrays.stream(dataCtor.getParameterInfo())
                        .filter(param -> param.getTypeDescriptor().toString()
                                .contains(TypesBuilderData.class.getName()))
                        .noneMatch(param -> Optional.ofNullable(param.getTypeDescriptor().getTypeAnnotationInfo())
                                .map(annotations -> annotations.stream()
                                        .anyMatch(annotation -> annotation.getName().contains(Nullable.class.getSimpleName())))
                                // the parameter type annotation disappears for non-static inner classes for some reason,
                                // fall back to normal parameter annotations
                                .orElseGet(() -> param.hasAnnotation(Nullable.class)))) {
                    issues.add(classPrefix + " TypesBuilderData parameter type doesn't have @Nullable annotation: " + dataCtor.toStringWithSimpleNames());
                }
            }
        }
        assertEquals(0, issues.size(), () ->
                "Found " + issues.size() + " issues: \n" + String.join("\n", issues) + "\n");
    }

    @Test
    @DisplayName("Test structure of registry container classes")
    public void testRegistryContainerClasses() {
        List<String> issues = new ArrayList<>();
        for (ClassInfo registry : SCAN_RESULT.getAllClasses()
                .filter(c -> c.getDeclaredFieldInfo().stream()
                        .filter(ClassMemberInfo::isStatic)
                        .anyMatch(field -> field.getTypeSignatureOrTypeDescriptor().toString()
                                .contains(VersionedRegistry.class.getName())))) {
            if (!registry.hasDeclaredField("REGISTRY")) {
                issues.add("No REGISTRY field present on class " + registry.toStringWithSimpleNames());
            }
            if (!registry.hasMethod("getRegistry")) {
                issues.add("No registry getter present on class " + registry.toStringWithSimpleNames());
            }
            if (!registry.isFinal()) {
                issues.add("Registry not marked as final: " + registry.toStringWithSimpleNames());
            }
            String classPrefix = '[' + registry.getName() + ']';
            MethodInfoList publicCtorInfo = registry.getConstructorInfo()
                    .filter(m -> !m.isPrivate());
            if (!publicCtorInfo.isEmpty()) {
                issues.add(classPrefix + " Registry has public constructor: " + publicCtorInfo);
            }
            for (MethodInfo define : registry.getMethodInfo("define")
                    .filter(m -> !m.isPrivate() && !m.hasAnnotation(Deprecated.class))) {
                if (!define.hasAnnotation(ApiStatus.Internal.class)) {
                    issues.add(classPrefix + " Public define method not marked as internal: " + define.toStringWithSimpleNames());
                }
            }
        }
        assertEquals(0, issues.size(), () ->
                "Found " + issues.size() + " issues: \n" + String.join("\n", issues) + "\n");
    }

    @Test
    @DisplayName("Ensure mappings get unloaded after initialization")
    public void ensureMappingDataUnload() {
        SCAN_RESULT.getAllClasses().stream()
                .flatMap(clazz -> clazz.getMethodInfo("getRegistry").stream())
                .filter(meth -> meth.isPublic() && meth.isStatic())
                .map(meth -> Assertions.assertDoesNotThrow(() -> {
                    // this also ensures registries are able to load without errors
                    return (VersionedRegistry<?>) meth.loadClassAndGetMethod().invoke(null);
                }))
                .forEach(registry -> Assertions.assertFalse(registry.isMappingDataLoaded(),
                        () -> "Mapping data for registry " + registry.getRegistryKey() + " is still loaded"));
    }
}
