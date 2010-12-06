/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.build.docs.dsl.docbook

import org.gradle.build.docs.dsl.TypeNameResolver
import org.gradle.build.docs.dsl.XmlSpecification
import org.gradle.build.docs.dsl.model.ClassMetaData
import org.gradle.build.docs.model.ClassMetaDataRepository

class JavadocLinkConverterTest extends XmlSpecification {
    final ClassMetaDataRepository<ClassMetaData> metaDataRepository = Mock()
    final TypeNameResolver nameResolver = Mock()
    final ClassMetaData classMetaData = Mock()
    final JavadocLinkConverter converter = new JavadocLinkConverter(document, nameResolver, metaDataRepository)

    def convertsLocalClassToApilinkElement() {
        when:
        def link = converter.resolve('someName', classMetaData)

        then:
        format(link) == '''<apilink class="org.gradle.SomeClass"/>'''
        _ * nameResolver.resolve('someName', classMetaData) >> 'org.gradle.SomeClass'
        _ * metaDataRepository.find('org.gradle.SomeClass') >> classMetaData
    }

    def convertsExternalClassToClassNameElement() {
        when:
        def link = converter.resolve('someName', classMetaData)

        then:
        format(link) == '''<classname>org.gradle.SomeClass</classname>'''
        _ * nameResolver.resolve('someName', classMetaData) >> 'org.gradle.SomeClass'
        _ * metaDataRepository.find('org.gradle.SomeClass') >> null

    }
    
    def resolvesUnknownFullyQualifiedClassName() {
        when:
        def link = converter.resolve('org.gradle.SomeClass', classMetaData)

        then:
        format(link) == '''<UNHANDLED-LINK>org.gradle.SomeClass</UNHANDLED-LINK>'''
    }
}