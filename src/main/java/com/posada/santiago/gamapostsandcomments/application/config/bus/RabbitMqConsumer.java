package com.posada.santiago.gamapostsandcomments.application.config.bus;


import com.google.gson.Gson;
import com.posada.santiago.gamapostsandcomments.application.config.bus.models.CommentModel;
import com.posada.santiago.gamapostsandcomments.application.config.bus.models.PostModel;
import com.posada.santiago.gamapostsandcomments.application.controller.SocketController;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqConsumer {
    private final Gson gson = new Gson();
    private final SocketController controller;

    public static final String PROXY_QUEUE_POST_CREATED = "events.proxy.post.created";
    public static final String PROXY_QUEUE_COMMENT_ADDED = "events.proxy.comment.added";

    public RabbitMqConsumer(SocketController controller) {
        this.controller = controller;
    }

    @RabbitListener(queues = PROXY_QUEUE_POST_CREATED)
    public void listenToPostCratedQueue(String message) throws ClassNotFoundException {
        PostModel post = gson.fromJson(message, PostModel.class);
        controller.sendPostCreated("mainSpace", post);
    }

    @RabbitListener(queues = PROXY_QUEUE_COMMENT_ADDED)
    public void listenToCommentAddedQueue(String message) throws ClassNotFoundException {
        CommentModel comment = gson.fromJson(message, CommentModel.class);
        controller.sendCommentAdded(comment.getPostId(), comment);
    }
}
