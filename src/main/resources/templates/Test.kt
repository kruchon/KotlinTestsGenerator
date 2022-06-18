<#macro parameterConstructorCallValues constructorCall><#if (constructorCall.childrenConstructorCalls?size > 0 && constructorCall.values?size > 0)>,</#if><#if (constructorCall.values?size = 1)>value = "${constructorCall.values[0]}"<#elseif (constructorCall.values?size > 1)> values = [<#list constructorCall.values as value>"${value}"<#if value?has_next>,</#if></#list>]</#if></#macro>
<#macro parameterConstructorCall constructorCall>${constructorCall.name}(<@parameterConstructorCallChildrenCalls constructorCall /><@parameterConstructorCallValues constructorCall />)</#macro>
<#macro parameterConstructorCallChildrenCalls constructorCall><#list constructorCall.childrenConstructorCalls as childrenConstructorCall>${childrenConstructorCall.name?lower_case} = <@parameterConstructorCall childrenConstructorCall /></#list></#macro>
package ${generationPackage}

<#list subjects as subject>import ${implementationPackage}.${subject}Impl<#if subject?has_next>${'\r\n'}</#if></#list>
<#list constructorCallNames as constructorCallName>import ${generationPackage}.${constructorCallName}<#if constructorCallName?has_next>${'\r\n'}</#if></#list>

class Test {
    @Test
    fun test() {
        <#list subjects as subject>
        val ${subject?lower_case} = ${subject}Impl()
        </#list>
        <#list functionCalls as functionCall>
        ${functionCall.contextObject}.run {
            ${functionCall.name}(${functionCall.constructorCall.name?lower_case} = <@parameterConstructorCall functionCall.constructorCall />)
        }
        </#list>
    }
}