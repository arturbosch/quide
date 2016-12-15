package io.gitlab.arturbosch.quide.platform

import io.gitlab.arturbosch.quide.model.CodeSmell
import io.gitlab.arturbosch.quide.model.SmellContainer
import io.gitlab.arturbosch.quide.platform.reflect.TypeToken
import spock.lang.Specification

/**
 * @author Artur Bosch
 */
class UserDataTest extends Specification {

	def data = new UserData() {}

	def "load stored value"() {
		when:
		data.put("test", "test")
		data.put("list", ["hello", "world"])
		then:
		data.get("test", TypeToken.get(String.class)).get() == "test"
		data.get("list", TypeToken.get(new TypeToken<SmellContainer<CodeSmell>>() {
		}.getType())).get() == ["hello", "world"]
	}

}
