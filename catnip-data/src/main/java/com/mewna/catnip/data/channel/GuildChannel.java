/*
 * Copyright (c) 2019 amy, All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.mewna.catnip.data.channel;

import com.mewna.catnip.data.guild.GuildEntity;
import com.mewna.catnip.data.guild.PermissionOverride;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * A channel in a guild.
 *
 * @author natanbc
 * @since 9/12/18
 */
@SuppressWarnings("unused")
public interface GuildChannel extends GuildEntity, Channel {
    /**
     * @return The name of the channel.
     */
    @Nonnull
    @CheckReturnValue
    String name();
    
    /**
     * @return The position of the channel.
     */
    @CheckReturnValue
    int position();
    
    /**
     * @return The id of the {@link Category} that is the parent of this
     * channel. May be {@code null}.
     */
    @Nullable
    @CheckReturnValue
    default String parentId() {
        final long id = parentIdAsLong();
        if(id == 0) {
            return null;
        }
        return Long.toUnsignedString(id);
    }
    
    /**
     * @return The id of the {@link Category} that is the parent of this
     * channel. A value of {@code 0} means no parent.
     */
    @CheckReturnValue
    long parentIdAsLong();
    
    /**
     * @return The permission overrides set on this channel. Will never be
     * {@code null}, but may be empty.
     */
    @Nonnull
    @CheckReturnValue
    List<PermissionOverride> overrides();
    
    @Override
    @CheckReturnValue
    default boolean isDM() {
        return false;
    }
    
    @Override
    @CheckReturnValue
    default boolean isGroupDM() {
        return false;
    }
    
    @Override
    default boolean isUserDM() {
        return false;
    }
    
    @Override
    @CheckReturnValue
    default boolean isGuild() {
        return true;
    }
}
