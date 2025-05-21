package com.projectalpha.controller.user.diner.list;

import com.projectalpha.dto.user.diner.custom_lists.CustomListRequest;
import com.projectalpha.dto.user.diner.custom_lists.listItem.CustomListItemRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ListsController {

    ResponseEntity<?> getDinerLists(@PathVariable String userId);

    ResponseEntity<?> createDinerList(@PathVariable String userId, @RequestBody CustomListRequest createRequest);

    ResponseEntity<?> updateDinerList(@PathVariable String userId, @PathVariable String listId, @RequestBody CustomListRequest updateRequest);

    ResponseEntity<?> removeDinerList(@PathVariable String userId, @PathVariable String listId);

    ResponseEntity<?> removeListItem(@PathVariable String userId, @PathVariable String listItemId);
}

