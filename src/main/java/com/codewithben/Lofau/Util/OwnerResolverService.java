package com.codewithben.Lofau.Util;

import com.codewithben.Lofau.User.model.User;
import com.codewithben.Lofau.media.enums.OwnerType;

import java.util.UUID;

public interface OwnerResolverService {

    User resolveOwner(
            UUID ownerId,
            OwnerType ownerType
    );
}