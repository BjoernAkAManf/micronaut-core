package io.micronaut.visitors

import io.micronaut.inject.AbstractTypeElementSpec
import io.micronaut.inject.writer.BeanDefinitionVisitor

class IntroductionVisitorSpec extends AbstractTypeElementSpec {

    void "test that it is possible to visit introduction advice that extend from existing interfaces"() {
        buildBeanDefinition('test.MyInterface' + BeanDefinitionVisitor.PROXY_SUFFIX, '''
package test;

import io.micronaut.aop.introduction.Stub;
import io.micronaut.visitors.InterfaceWithGenerics;

@Stub
interface MyInterface extends InterfaceWithGenerics<Foo, Long>  {
    String myMethod();
}

class Foo {}
''')
        expect:
        IntroductionVisitor.VISITED_CLASS_ELEMENTS.size() == 1
        IntroductionVisitor.VISITED_METHOD_ELEMENTS.size() == 2
        IntroductionVisitor.VISITED_METHOD_ELEMENTS[1].name == 'save'
        IntroductionVisitor.VISITED_METHOD_ELEMENTS[1].genericReturnType.name == 'test.Foo'
        IntroductionVisitor.VISITED_METHOD_ELEMENTS[1].parameters[0].genericType.name == 'test.Foo'
    }

}