package io.klib.chat.provider;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.klib.chat.api.Chat;
import io.klib.chat.api.Message;
import osgi.enroute.debug.api.Debug;

@Component(
	property = { 
		Debug.COMMAND_SCOPE + "=zhat", 
		Debug.COMMAND_FUNCTION + "=chat",
		Debug.COMMAND_FUNCTION + "=members",
		Debug.COMMAND_FUNCTION + "=send"
	},
	service = ChatCommand.class
)
public class ChatCommand {

	public String chat() {
		return "chat                         help\n"
		+      "send <from> <to> <text>      send a message\n"
		+      "members                      get list of members\n";
	}

	public boolean send(String from, String to, String text) throws Exception {
		  Chat chat = members.get(to);
		  if (chat != null) {
			Message message = new Message();
		    message.from = from;
		    message.to = to;
		    message.text = text;
		    return chat.send(message);
		  }
		  return false;
	}
	
	private Map<String, Chat> members = new ConcurrentHashMap<>();

	public Collection<String> members() {
	  return members.keySet().stream().sorted().collect(Collectors.toList());
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE, 
			policy = ReferencePolicy.DYNAMIC
	)
	void addChat(Chat member, Map<String, Object> map) {
	  String userName = getUserName(map);
	  if (userName != null)
	    members.put(userName, member);
	}

	void removeChat(Chat member, Map<String, Object> map) {
	  String userName = getUserName(map);
	  if (userName != null)
	    members.remove(userName);
	}

	private String getUserName(Map<String, Object> map) {
	  return (String) map.get(Chat.USER_NAME);
	}
	
}
