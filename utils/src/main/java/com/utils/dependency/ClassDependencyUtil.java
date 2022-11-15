package com.utils.dependency;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import com.google.common.collect.Sets;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lichaoshuai
 * Created on 2022-11-14
 */
@Slf4j
public class ClassDependencyUtil {

    /**
     * 项目的package路径
     */
    public static final String PROJECT_BASE_PACKAGE = "com.utils.dependency";

    /**
     * 扫描需要排除掉的package前缀
     */
    private static final Set<String> EXCLUDE_PACKAGE_PREFIX = Sets.newHashSet(
            "java.",
            "lombok.",
            "javax."
    );


    public static void main(String[] args) {
        final Class<?>[] allRpcClass = getAllClass();

        final Map<String, Set<String>> collect = Arrays.stream(allRpcClass)
                .flatMap(clazz -> {
                    try {
                        return JavassistUtil.getAllDependency(clazz.getName())
                                .stream()
                                .filter(relyClazz -> EXCLUDE_PACKAGE_PREFIX.stream()
                                        .noneMatch(relyClazz::startsWith))
                                .map(relyClazz -> {
                                    try {
                                        final Class<?> aClass = Thread.currentThread()
                                                .getContextClassLoader()
                                                .loadClass(relyClazz);

                                        final URL location = aClass.getProtectionDomain()
                                                .getCodeSource()
                                                .getLocation();
                                        return ClassJar.builder()
                                                .className(relyClazz)
                                                .jarName(location.getPath())
                                                .build();
                                    } catch (Exception ex) {
                                        return ClassJar.builder()
                                                .className(relyClazz)
                                                .jarName("")
                                                .build();
                                    }
                                });
                    } catch (Exception ex) {
                        log.info("JavassistUtil getAllDependency error ===============> {}", clazz.getName());
                        return Stream.empty();
                    }
                })
                .collect(Collectors.groupingBy(ClassJar::getJarName,
                        Collectors.mapping(ClassJar::getClassName, Collectors.toSet())));

        print(collect);

    }

    private static void print(Map<String, Set<String>> map) {
        for (Entry<String, Set<String>> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            for (String className : entry.getValue()) {
                System.out.println("       " + className);
            }
        }
    }

    private static Class<?>[] getAllClass() {
        List<Class<?>> classList = new ArrayList<>();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + ClassUtils
                .convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(PROJECT_BASE_PACKAGE))
                + File.separator + "**/*.class";

        log.info("scan class path: {}", packageSearchPath);
        try {
            Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                String className = metadataReader.getClassMetadata()
                        .getClassName();
                Class<?> clazz = Thread.currentThread()
                        .getContextClassLoader()
                        .loadClass(className);
                classList.add(clazz);
            }
        } catch (Exception e) {
            log.error("scan class failed ===========> {}", e.getMessage(), e);
        }
        Class<?>[] classArr = new Class[classList.size()];
        classList.toArray(classArr);
        return classArr;
    }

    @Data
    @Builder
    static class ClassJar {
        private String className;
        private String jarName;
    }

}
