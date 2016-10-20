package io.klib.chat.provider;

import org.osgi.service.component.annotations.Component;

import io.klib.chat.api.Chat;
import io.klib.chat.api.Message;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Designate;

@ObjectClassDefinition
@interface Configuration {
	String user_name() default "osgi";
}

@Designate(ocd=Configuration.class)
@Component(
		name = "io.klib.chat", 
		property = "user.name=osgi"
)
public class ChatImpl implements Chat {

	@Override
	public boolean send(Message message) throws Exception {
		System.out.printf("%s: %s%n", message.from, message.text);
		return true;
	}

}
