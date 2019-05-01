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

package com.mewna.catnip.data.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mewna.catnip.data.CatnipEntity;
import com.mewna.catnip.data.Snowflake;
import com.mewna.catnip.data.guild.Member;
import com.mewna.catnip.data.guild.Role;
import com.mewna.catnip.data.misc.Emoji;
import com.mewna.catnip.data.user.User;
import org.immutables.value.Value.Modifiable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * A single message in Discord.
 *
 * @author amy
 * @since 9/4/18.
 */
@SuppressWarnings("unused")
@Modifiable
@CatnipEntity
public interface Message extends Snowflake {
    /**
     * The type of message. Use this to tell normal messages from system messages.
     *
     * @return enum representing the message type. Never null.
     */
    @Nonnull
    @CheckReturnValue
    MessageType type();
    
    /**
     * Whether or not the message was sent using the /TTS command, making clients read
     * <br>the message aloud using their text-to-speech engine.
     *
     * @return True if the message is to be spoken, false otherwise.
     */
    @CheckReturnValue
    boolean tts();
    
    /**
     * When the message was sent.
     *
     * @return {@link OffsetDateTime Date and time} the message was sent.
     */
    @Nonnull
    @CheckReturnValue
    OffsetDateTime timestamp();
    
    /**
     * Whether the message is pinned.
     * <br>This will always be false on new messages.
     *
     * @return True if the message is pinned, false otherwise.
     */
    @CheckReturnValue
    boolean pinned();
    
    /**
     * Whether the message mentions everyone.
     *
     * @return True if the message mentions everyone, false otherwise.
     */
    @CheckReturnValue
    boolean mentionsEveryone();
    
    /**
     * The message's nonce snowflake.
     * <br>Nonces are used to validating messages have been sent, as two identical messages
     * <br>with the same nonce are considered to be re-sent due to network errors.
     *
     * @return unique String nonce for the message. Can be null.
     */
    @Nullable
    @CheckReturnValue
    String nonce();
    
    /**
     * List of users @mentioned by this message.
     *
     * @return List of Users. Never null.
     */
    @Nonnull
    @CheckReturnValue
    List<User> mentionedUsers();
    
    /**
     * A list of members mentioned by this message. Will contain the same users
     * as {@link #mentionedUsers()}. Will always be empty for DMs.
     *
     * @return List of members. Never null.
     */
    @Nonnull
    @CheckReturnValue
    List<Member> mentionedMembers();
    
    /**
     * List of roles @mentioned by this message.
     * <br>All users with these roles will also be mentioned
     *
     * @return List of Roles. Never null.
     */
    @Nonnull
    @CheckReturnValue
    List<Role> mentionedRoles();
    
    /**
     * The author of the message, as a member of the guild.
     * <br>Can be null if the author is no longer in the guild, or if the message was sent
     * <br>by a fake user.
     *
     * @return Member representation of the {@link Message#author() author}. Can be null.
     */
    @Nullable
    @CheckReturnValue
    Member member();
    
    /**
     * List of embeds in the message.
     *
     * @return List of embeds. Never null.
     */
    @Nonnull
    @CheckReturnValue
    List<Embed> embeds();
    
    /**
     * When the message was last edited, if ever.
     * <br>Previous edits are not exposed, only the most recent.
     *
     * @return The {@link OffsetDateTime date and time} the message was last edited. Null if the message was never edited.
     */
    @Nullable
    @CheckReturnValue
    OffsetDateTime editedTimestamp();
    
    /**
     * The message's content. Is just an empty string for embed-only messages.
     *
     * @return String containing the message body. Never null.
     */
    @Nonnull
    @CheckReturnValue
    String content();
    
    /**
     * The snowflake ID of the channel this message was sent in.
     *
     * @return String representing the channel ID. Never null.
     */
    @Nonnull
    @CheckReturnValue
    default String channelId() {
        return Long.toUnsignedString(channelIdAsLong());
    }
    
    /**
     * The snowflake ID of the channel this message was sent in.
     *
     * @return String representing the channel ID. Never null.
     */
    @CheckReturnValue
    long channelIdAsLong();
    
    /**
     * Guild-agnostic representation of the author of the message.
     *
     * @return The author of the message. Never null.
     *
     * @see Message#member() Guild-specific representation of the author.
     */
    @Nonnull
    @CheckReturnValue
    User author();
    
    /**
     * List of files sent with the message.
     *
     * @return List of files sent with the message. Never null.
     */
    @Nonnull
    @CheckReturnValue
    List<Attachment> attachments();
    
    /**
     * List of reactions added to the message.
     * <br>This list will <b>not</b> be updated.
     *
     * @return List of reactions added to the message. Never null.
     */
    @Nonnull
    @CheckReturnValue
    List<Reaction> reactions();
    
    /**
     * The snowflake ID of the guild this message was sent in.
     *
     * @return String representing the guild ID. Null if sent in DMs.
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
     * The snowflake ID of the guild this message was sent in.
     *
     * @return Long representing the guild ID. Null if sent in DMs.
     */
    @CheckReturnValue
    long guildIdAsLong();
    
    /**
     * The snowflake ID of the webhook this message was sent by.
     *
     * @return String representing the webhook ID. Null if not sent by a webhook.
     */
    @Nullable
    @CheckReturnValue
    default String webhookId() {
        final long id = webhookIdAsLong();
        if(id == 0) {
            return null;
        }
        return Long.toUnsignedString(id);
    }
    
    /**
     * The snowflake ID of the webhook this message was sent by.
     *
     * @return Long representing the webhook ID. {@code 0} if not sent by a webhook.
     */
    @CheckReturnValue
    long webhookIdAsLong();
    
    @JsonIgnore
    default boolean isRickRoll() {
        return content().contains("https://www.youtube.com/watch?v=dQw4w9WgXcQ") || content().contains("https://youtu.be/dQw4w9WgXcQ");
    }
    
    @Modifiable
    @CatnipEntity
    interface Attachment extends Snowflake {
        /**
         * The name of the file represented by this attachment.
         *
         * @return String representing the file name. Never null.
         */
        @Nonnull
        @CheckReturnValue
        String fileName();
        
        /**
         * The size of the file represented by this attachment, in bytes.
         *
         * @return Integer representing the file size. Never negative.
         */
        @Nonnegative
        @CheckReturnValue
        int size();
        
        /**
         * The source URL for the file.
         *
         * @return String representing the source URL. Never null.
         */
        @Nonnull
        @CheckReturnValue
        String url();
        
        /**
         * The proxied URL for the file.
         *
         * @return String representing the proxied URL. Never null.
         */
        @Nonnull
        @CheckReturnValue
        String proxyUrl();
        
        /**
         * The height of this attachment, if it's an image.
         *
         * @return Integer representing the height, or -1 if this attachment is not an image.
         */
        @CheckReturnValue
        int height();
        
        /**
         * The width of this attachment, if it's an image.
         *
         * @return Integer representing the width, or -1 if this attachment is not an image.
         */
        @CheckReturnValue
        int width();
        
        /**
         * Whether this attachment is an image.
         *
         * @return True if this attachment is an image, false otherwise.
         */
        @CheckReturnValue
        default boolean image() {
            return height() > 0 && width() > 0;
        }
    }
    
    @Modifiable
    @CatnipEntity
    interface Reaction {
        /**
         * The count of reactions.
         *
         * @return Integer representing how many reactions were added.
         */
        @Nonnegative
        @CheckReturnValue
        int count();
        
        /**
         * Whether the current logged in account added this reaction.
         *
         * @return True if the current account added this reaction, false otherwise.
         */
        @CheckReturnValue
        boolean self();
        
        /**
         * The emojis representing this reaction.
         *
         * @return Emoji object of this reaction.
         */
        @Nonnull
        @CheckReturnValue
        Emoji emoji();
    }
}
