package im.conversations.android.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.common.base.Preconditions;
import im.conversations.android.database.model.Modification;
import im.conversations.android.transformer.Transformation;
import im.conversations.android.xmpp.model.stanza.Message;
import java.time.Instant;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.parts.Resourcepart;

@Entity(
        tableName = "message_version",
        foreignKeys = {
            @ForeignKey(
                    entity = MessageEntity.class,
                    parentColumns = {"id"},
                    childColumns = {"messageEntityId"},
                    onDelete = ForeignKey.CASCADE),
        },
        indices = {@Index(value = "messageEntityId")})
public class MessageVersionEntity {

    @PrimaryKey(autoGenerate = true)
    public Long id;

    @NonNull public long messageEntityId;
    public String messageId;
    public String stanzaId;
    public Modification modification;
    public BareJid modifiedBy;
    public Resourcepart modifiedByResource;
    public String occupantId;
    public Instant receivedAt;

    // the version order is determined by the receivedAt
    // the actual display time and display order comes from the parent MessageEntity
    // the original has a receivedAt = null and stanzaId = null and inherits it's timestamp from
    // it's parent

    public static MessageVersionEntity of(
            long messageEntityId,
            final Modification modification,
            final Transformation transformation) {
        if (transformation.type == Message.Type.GROUPCHAT
                && modification != Modification.ORIGINAL) {
            Preconditions.checkState(
                    transformation.occupantId != null,
                    "Message versions other than original must have an occupantId in group chats");
        }
        final var entity = new MessageVersionEntity();
        entity.messageEntityId = messageEntityId;
        entity.messageId = transformation.messageId;
        entity.stanzaId = transformation.stanzaId;
        entity.modification = modification;
        entity.modifiedBy = transformation.fromBare();
        entity.modifiedByResource = transformation.fromResource();
        entity.occupantId = transformation.occupantId;
        entity.receivedAt = transformation.receivedAt;
        return entity;
    }
}
