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

import com.mewna.catnip.data.CatnipEntity;
import com.mewna.catnip.data.Snowflake;
import com.mewna.catnip.util.CDNFormat;
import com.mewna.catnip.util.ImageOptions;
import com.mewna.catnip.util.Permission;
import org.immutables.value.Value.Modifiable;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

/**
 * Represents a Discord guild. A guild is colloquially referred to as a server,
 * both in the client UI and by users.
 *
 * @author natanbc
 * @since 9/6/18
 */
@SuppressWarnings("unused")
@Modifiable
@CatnipEntity
public interface Guild extends Snowflake {
    
    int NICKNAME_MAX_LENGTH = 32;
    
    /**
     * @return The guild's name.
     */
    @Nonnull
    @CheckReturnValue
    String name();
    
    /**
     * @return The hash of the guild's icon.
     */
    @Nullable
    @CheckReturnValue
    String icon();
    
    /**
     * Return the guild's icon's CDN URL with the specified options.
     *
     * @param options The options to configure the URL returned.
     *
     * @return The CDN URL for the guild's icon.
     */
    @Nullable
    @CheckReturnValue
    default String iconUrl(@Nonnull final ImageOptions options) {
        return CDNFormat.iconUrl(id(), icon(), options);
    }
    
    /**
     * @return The CDN URL for the guild's icon.
     */
    @Nullable
    @CheckReturnValue
    default String iconUrl() {
        return iconUrl(new ImageOptions());
    }
    
    /**
     * @return The splash image for the guild. May be {@code null}.
     */
    @Nullable
    @CheckReturnValue
    String splash();
    
    /**
     * The CDN URL of the guild's splash image.
     *
     * @param options The options to configure the URL returned.
     *
     * @return The CDN URL.
     */
    @Nullable
    @CheckReturnValue
    default String splashUrl(@Nonnull final ImageOptions options) {
        return CDNFormat.splashUrl(id(), splash(), options);
    }
    
    /**
     * @return The CDN URL of the guild's splash image.
     */
    @Nullable
    @CheckReturnValue
    default String splashUrl() {
        return splashUrl(new ImageOptions());
    }
    
    /**
     * @return Whether the guild is owned by the current user.
     */
    @CheckReturnValue
    boolean owned();
    
    /**
     * @return The id of the user who owns the guild.
     */
    @Nonnull
    @CheckReturnValue
    default String ownerId() {
        return Long.toUnsignedString(ownerIdAsLong());
    }
    
    /**
     * @return The id of the user who owns the guild.
     */
    @CheckReturnValue
    long ownerIdAsLong();
    
    /**
     * @return Total permissions for the user in the guild. Does NOT include
     * channel overrides.
     */
    @Nonnull
    @CheckReturnValue
    Set<Permission> permissions();
    
    /**
     * @return The region that the guild's voice servers are located in.
     */
    @Nonnull
    @CheckReturnValue
    String region();
    
    /**
     * @return The id of the afk voice channel for the guild.
     */
    @Nullable
    @CheckReturnValue
    default String afkChannelId() {
        final long id = afkChannelIdAsLong();
        if(id == 0) {
            return null;
        }
        return Long.toUnsignedString(id);
    }
    
    /**
     * @return The id of the afk voice channel for the guild.
     */
    @CheckReturnValue
    long afkChannelIdAsLong();
    
    /**
     * @return The amount of time a user must be afk for before they're moved
     * to the afk channel.
     */
    @CheckReturnValue
    int afkTimeout();
    
    /**
     * @return Whether the guild embed is enabled.
     */
    @CheckReturnValue
    boolean embedEnabled();
    
    /**
     * @return The channel the guild embed is for, if enabled.
     */
    @Nullable
    @CheckReturnValue
    default String embedChannelId() {
        final long id = embedChannelIdAsLong();
        if(id == 0) {
            return null;
        }
        return Long.toUnsignedString(id);
    }
    
    /**
     * @return The channel the guild embed is for, if enabled.
     */
    @CheckReturnValue
    long embedChannelIdAsLong();
    
    /**
     * @return The verification level set for the guild.
     */
    @Nonnull
    @CheckReturnValue
    VerificationLevel verificationLevel();
    
    /**
     * @return The notification level set for the guild.
     */
    @Nonnull
    @CheckReturnValue
    NotificationLevel defaultMessageNotifications();
    
    /**
     * @return The explicit content filter level set for the guild.
     */
    @Nonnull
    @CheckReturnValue
    ContentFilterLevel explicitContentFilter();
    
    /**
     * @return The list of features enabled for the guild.
     */
    @Nonnull
    @CheckReturnValue
    List<String> features();
    
    /**
     * @return The MFA level set for guild administrators.
     */
    @Nonnull
    @CheckReturnValue
    MFALevel mfaLevel();
    
    /**
     * @return The id of the application that created this guild.
     */
    @Nullable
    @CheckReturnValue
    default String applicationId() {
        final long id = applicationIdAsLong();
        if(id == 0) {
            return null;
        }
        return Long.toUnsignedString(id);
    }
    
    /**
     * @return The id of the application that created this guild.
     */
    @CheckReturnValue
    long applicationIdAsLong();
    
    /**
     * @return Whether or not the guild's widget is enabled.
     */
    @CheckReturnValue
    boolean widgetEnabled();
    
    /**
     * @return The channel the guild's widget is set for, if enabled.
     */
    @Nullable
    @CheckReturnValue
    default String widgetChannelId() {
        final long id = widgetChannelIdAsLong();
        if(id == 0) {
            return null;
        }
        return Long.toUnsignedString(id);
    }
    
    /**
     * @return The channel the guild's widget is set for, if enabled.
     */
    @CheckReturnValue
    long widgetChannelIdAsLong();
    
    /**
     * @return The id of the channel used for system messages (ex. the built-in
     * member join messages).
     */
    @Nullable
    @CheckReturnValue
    default String systemChannelId() {
        final long id = systemChannelIdAsLong();
        if(id == 0) {
            return null;
        }
        return Long.toUnsignedString(id);
    }
    
    /**
     * @return The id of the channel used for system messages (ex. the built-in
     * member join messages).
     */
    @CheckReturnValue
    long systemChannelIdAsLong();
    
    //The following fields are only sent in GUILD_CREATE
    
    /**
     * @return The date/time that the current user joined the guild at.
     */
    @CheckReturnValue
    OffsetDateTime joinedAt();
    
    /**
     * @return Whether or not this guild is considered "large."
     */
    @CheckReturnValue
    boolean large();
    
    /**
     * @return The maximum number of presences the guild can have. Will be
     * {@code 0} if no value was present.
     */
    @Nonnegative
    @CheckReturnValue
    int maxPresences();
    
    /**
     * @return The maximum number of members the guild can have. Will be
     * {@code 0} if no value was present.
     */
    @Nonnegative
    @CheckReturnValue
    int maxMembers();
    
    /**
     * @return The vanity invite code for this guild, ie.
     * {@code discord.gg/vanity_code}.
     */
    @Nullable
    @CheckReturnValue
    String vanityUrlCode();
    
    /**
     * @return The guild's description.
     */
    // TODO: What actually is this?
    @Nullable
    @CheckReturnValue
    String description();
    
    /**
     * @return The guild's banner hash.
     *
     * @apiNote See https://discordapp.com/developers/docs/reference#image-formatting "Guild Banner"
     */
    @Nullable
    @CheckReturnValue
    String banner();
    
    /**
     * The notification level for a guild.
     */
    enum NotificationLevel {
        /**
         * Users get notifications for all messages.
         */
        ALL_MESSAGES(0),
        /**
         * Users only get notifications for mentions.
         */
        ONLY_MENTIONS(1);
        
        private final int key;
        
        NotificationLevel(final int key) {
            this.key = key;
        }
        
        @Nonnull
        public static NotificationLevel byKey(final int key) {
            for(final NotificationLevel level : values()) {
                if(level.key == key) {
                    return level;
                }
            }
            throw new IllegalArgumentException("No verification level for key " + key);
        }
        
        public int getKey() {
            return key;
        }
    }
    
    /**
     * The content filter level for a guild.
     */
    enum ContentFilterLevel {
        /**
         * No messages are filtered.
         */
        DISABLED(0),
        /**
         * Only messages from members with no roles are filtered.
         */
        MEMBERS_WITHOUT_ROLES(1),
        /**
         * Messages from all members are filtered.
         */
        ALL_MEMBERS(2);
        
        private final int key;
        
        ContentFilterLevel(final int key) {
            this.key = key;
        }
        
        @Nonnull
        public static ContentFilterLevel byKey(final int key) {
            for(final ContentFilterLevel level : values()) {
                if(level.key == key) {
                    return level;
                }
            }
            throw new IllegalArgumentException("No content filter level for key " + key);
        }
        
        public int getKey() {
            return key;
        }
    }
    
    /**
     * The 2FA level required for this guild.
     */
    enum MFALevel {
        /**
         * 2FA is not required.
         */
        NONE(0),
        /**
         * 2FA is required for admin actions.
         */
        ELEVATED(1);
        
        private final int key;
        
        MFALevel(final int key) {
            this.key = key;
        }
        
        @Nonnull
        public static MFALevel byKey(final int key) {
            for(final MFALevel level : values()) {
                if(level.key == key) {
                    return level;
                }
            }
            throw new IllegalArgumentException("No MFA level for key " + key);
        }
        
        public int getKey() {
            return key;
        }
    }
    
    /**
     * The verification level for a guild.
     */
    enum VerificationLevel {
        /**
         * No restrictions.
         */
        NONE(0),
        /**
         * Members must have a verified email on their account.
         */
        LOW(1),
        /**
         * Members must also be registered on Discord for more than 5 minutes.
         */
        MEDIUM(2),
        /**
         * Members must also have been a member of this guild for more than 10
         * minutes.
         */
        HIGH(3),
        /**
         * Members must also have a verified phone number on their account.
         */
        VERY_HIGH(4);
        
        private final int key;
        
        VerificationLevel(final int key) {
            this.key = key;
        }
        
        @Nonnull
        public static VerificationLevel byKey(final int key) {
            for(final VerificationLevel level : values()) {
                if(level.key == key) {
                    return level;
                }
            }
            throw new IllegalArgumentException("No verification level for key " + key);
        }
        
        public int getKey() {
            return key;
        }
    }
}
