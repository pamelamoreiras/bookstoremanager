package com.pamelamoreiras.bookstoremanager.users.utils;

import com.pamelamoreiras.bookstoremanager.users.dto.MessageDTO;
import com.pamelamoreiras.bookstoremanager.users.entity.User;

public class MessageDTOUtils {

    public static MessageDTO creationMessage(final User createdUser) {
        return returnMessage(createdUser,"created");
    }

    public static MessageDTO updatedMessage(final User updatedUser) {
        return returnMessage(updatedUser,"updated");
    }

    public static MessageDTO returnMessage(final User updatedUser, final String action) {
        final var updatedUserName = updatedUser.getUsername();
        final var updatedUserId = updatedUser.getId();
        final var updatedUserMessage = String.format("User %s with ID %s successfully %s", updatedUserName, updatedUserId, action);

        return MessageDTO.builder()
                .message(updatedUserMessage)
                .build();
    }
}
