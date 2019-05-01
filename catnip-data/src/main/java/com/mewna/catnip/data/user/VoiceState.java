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

package com.mewna.catnip.data.user;

import com.mewna.catnip.data.CatnipEntity;
import com.mewna.catnip.data.Entity;
import org.immutables.value.Value.Modifiable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A user's voice state.
 *
 * @author amy
 * @since 9/21/18.
 */
@Modifiable
@CatnipEntity
public interface VoiceState extends Entity {
    /**
     * @return The id of the guild this voice state is for, if applicable.
     */
    @Nullable
    @CheckReturnValue
    default String guildId() {
        final long id = guildIdAsLong();
        if(id == 0) {
            return null;
        }
        return Long.toUnsignedString(id);
    }
    
    /**
     * @return The id of the guild this voice state is for, if applicable.
     */
    @CheckReturnValue
    long guildIdAsLong();
    
    /**
     * @return The channel the user is connected to, if applicable.
     */
    @Nullable
    @CheckReturnValue
    default String channelId() {
        final long id = channelIdAsLong();
        if(id == 0) {
            return null;
        }
        return Long.toUnsignedString(id);
    }
    
    /**
     * @return The channel the user is connected to, if applicable.
     */
    @CheckReturnValue
    long channelIdAsLong();
    
    /**
     * @return The user's id.
     */
    @Nonnull
    @CheckReturnValue
    default String userId() {
        return Long.toUnsignedString(userIdAsLong());
    }
    
    /**
     * @return The user's id.
     */
    @CheckReturnValue
    long userIdAsLong();
    
    /**
     * @return The session id for this voice state. Only known for the current
     * user.
     */
    @Nullable
    @CheckReturnValue
    String sessionId();
    
    /**
     * @return Whether the user has been deafened.
     */
    @CheckReturnValue
    boolean deaf();
    
    /**
     * @return Whether the user has been muted.
     */
    @CheckReturnValue
    boolean mute();
    
    /**
     * @return Whether the user has deafened themself.
     */
    @CheckReturnValue
    boolean selfDeaf();
    
    /**
     * @return Whether the user has muted themself.
     */
    @CheckReturnValue
    boolean selfMute();
    
    /**
     * @return Whether the user has been suppressed.
     */
    @CheckReturnValue
    boolean suppress();
}
