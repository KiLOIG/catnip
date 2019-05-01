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

package com.mewna.catnip.data.guild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mewna.catnip.data.CatnipEntity;
import com.mewna.catnip.data.Mentionable;
import org.immutables.value.Value.Modifiable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.OffsetDateTime;
import java.util.Set;

/**
 * A member in a guild.
 *
 * @author amy
 * @since 9/4/18.
 */
@SuppressWarnings("unused")
@Modifiable
@CatnipEntity
public interface Member extends Mentionable, PermissionHolder {
    /**
     * The user's nickname in this guild.
     *
     * @return User's nickname. Null if not set.
     */
    @Nullable
    @CheckReturnValue
    String nick();
    
    /**
     * The ids of the user's roles in this guild.
     *
     * @return A {@link Set} of the ids of the user's roles.
     */
    @Nonnull
    @CheckReturnValue
    Set<String> roleIds();
    
    /**
     * Whether the user is voice muted.
     * <br>Voice muted user cannot transmit voice.
     *
     * @return True if muted, false otherwise.
     */
    @CheckReturnValue
    boolean mute();
    
    /**
     * Whether the user is deafened.
     * <br>Deafened users cannot receive nor send voice.
     *
     * @return True if deafened, false otherwise.
     */
    @CheckReturnValue
    boolean deaf();
    
    /**
     * When the user joined the server last.
     * <br>Members who have joined, left, then rejoined will only have the most
     * recent join exposed.
     * <br>This may be null under some conditions, ex. a member leaving a
     * guild. In cases like this, catnip will attempt to load the old data from
     * the cache if possible, but it may not work, hence nullability.
     *
     * @return The {@link OffsetDateTime date and time} the member joined the guild.
     */
    @Nullable
    @CheckReturnValue
    OffsetDateTime joinedAt();
    
    /**
     * @return A mention for this member that can be sent in a message.
     */
    @Nonnull
    @JsonIgnore
    @CheckReturnValue
    default String asMention() {
        return nick() != null ? "<@!" + id() + '>' : "<@" + id() + '>';
    }
}
