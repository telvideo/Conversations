package im.conversations.android.xmpp.model.retract;

import im.conversations.android.annotation.XmlElement;
import im.conversations.android.xmpp.model.Extension;

@XmlElement
public class Retracted extends Extension {

    public Retracted() {
        super(Retracted.class);
    }
}