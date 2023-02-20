package im.conversations.android.xmpp.model.blocking;

import im.conversations.android.annotation.XmlElement;
import im.conversations.android.xmpp.model.Extension;
import org.jxmpp.jid.Jid;

@XmlElement
public class Item extends Extension {

    public Item() {
        super(Item.class);
    }

    public Jid getJid() {
        return getAttributeAsJid("jid");
    }
}