package org.rmcc.ccc.service.currentuser;

import org.rmcc.ccc.model.CurrentUser;

public interface CurrentUserService {

    boolean canAccessUser(CurrentUser currentUser, Long userId);

}