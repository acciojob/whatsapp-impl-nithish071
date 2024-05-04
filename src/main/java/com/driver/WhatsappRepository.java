package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;

    private HashMap<Integer,Message> messageHashMap;

    private List<User> userList;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.userList = new ArrayList<>();
        this.messageId = 0;
        this.messageHashMap = new HashMap<>();
    }

    public String createUser(String name, String mobile) throws Exception {
        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }
        User user = new User(name,mobile);
        this.userList.add(user);
        this.userMobile.add(mobile);
        return "Success";
    }


    public Group createGroup(List<User> users) {
        String groupName = "";
        if(users.size() > 2){
            customGroupCount++;
            groupName = "Group "+customGroupCount;
        } else {
            groupName = users.get(1).getName();
        }
        Group group = new Group(groupName,users.size());
        this.groupUserMap.put(group,users);
        this.adminMap.put(group,users.get(0));
        return group;
    }

    public int createMessage(String content) {
        messageId++;
        Message message = new Message(messageId,content);
        message.setTimestamp(new Date());
        this.messageHashMap.put(messageId,message);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!this.groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        boolean isMember = false;
        List<User> users = this.groupUserMap.get(group);
        for(User user: users){
            if(user.getMobile().equals(sender.getMobile())){
                isMember = true;
                break;
            }
        }
        if(!isMember){
            throw new Exception("You are not allowed to send message");
        }
        if(!groupMessageMap.containsKey(group)){
            List<Message> msg = new ArrayList<>();
            msg.add(message);
            this.groupMessageMap.put(group,msg);
        } else {
            List<Message> msg = this.groupMessageMap.get(group);
            msg.add(message);
            this.groupMessageMap.put(group,msg);
        }
        this.senderMap.put(message,sender);
        return this.groupMessageMap.get(group).size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!this.groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        if(!this.adminMap.get(group).getMobile().equals(approver.getMobile())){
            throw new Exception("Approver does not have rights");
        }
        boolean isMember = false;
        List<User> users = this.groupUserMap.get(group);
        for(User user1: users){
            if(user1.getMobile().equals(user.getMobile())){
                isMember = true;
                break;
            }
        }
        if(!isMember){
            throw new Exception("User is not a participant");
        }
        this.adminMap.put(group,user);
        return "SUCCESS";
    }

    public String findMessage(Date start, Date end, int k) {
        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception {
        boolean isUser = false;
        Group group = null;
        for(Group grp: groupUserMap.keySet()){
            List<User> usr = groupUserMap.get(grp);
            for(User user1:usr){
                if(user1.getMobile().equals(user.getMobile())){
                    group = grp;
                    isUser = true;
                    break;
                }
            }
        }
        if(!isUser){
            throw new Exception("User not found");
        }
        User admin = adminMap.get(group);
        if(admin.getMobile().equals(user.getMobile())){
            throw new Exception("Cannot remove admin");
        }
        return 0;
    }
}
