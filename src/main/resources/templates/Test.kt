<#macro parameterConstructorCall constructorCall>${constructorCall.name}(<#list constructorCall.childrenConstructorCalls as childrenConstructorCall>${childrenConstructorCall.name?lower_case} = <@parameterConstructorCall childrenConstructorCall />,<#else>values = [<#list constructorCall.values as value>"${value}",</#list>]</#list>)</#macro>
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