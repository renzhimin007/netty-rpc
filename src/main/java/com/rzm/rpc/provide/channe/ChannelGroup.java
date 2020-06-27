package com.rzm.rpc.provide.channe;


import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class ChannelGroup {




    static Map<String,Channel> map = new HashMap<String, Channel>(2);

    static Random random = new Random();

    public void add(Channel channel){
        map.put(channel.id().asLongText(),channel);
    }



    public Channel get(String id){
       return map.get(id);
    }


    public Channel randomChoice(){
        Collection<Channel> values = map.values();
        if (values.isEmpty()){
            return null;
        }
        Channel[] chans = new Channel[0];
        values.toArray(chans);
        return chans[random.nextInt(chans.length)];
    }



    public void del(Channel channel){
        map.remove(channel.id().asLongText());
    }
}
