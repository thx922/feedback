package dnn.service.user.session;

import dnn.entity.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by huanghuanlai on 16/6/14.
 */
public class SessionQuartz extends Thread{

    private static final Logger CONSOLE = LoggerFactory.getLogger(SessionQuartz.class);

    private static final int POLL_MIN = 1;//循环时间(分)
    private static final int SESSION_TIMEOUT = 3;//会话过期时间
    private Map<String,Online> sessions;
    public SessionQuartz(Map<String,Online> sessions){
        this.sessions = sessions;
    }

    @Override
    public void run() {
        try {
            for(;;){
                for(Map.Entry<String,Online> entry : sessions.entrySet()){
                    if(entry.getValue().getLastAccessTime().plusMinutes(SESSION_TIMEOUT).isBefore(LocalDateTime.now())){
                        CONSOLE.info("remove token:"+entry.getKey());
                        sessions.remove(entry.getKey());
                    }
                }
                TimeUnit.MINUTES.sleep(POLL_MIN);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
