package RaisePig.powers;

import RaisePig.Helper.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TruffleHunterPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("TruffleHunterPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean upgraded;

    public TruffleHunterPower(AbstractCreature owner, int amount, boolean upgraded) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.upgraded = upgraded;
        this.type = PowerType.BUFF;

        String path128 = "RaisePigResources/img/powers/Example84.png";
        String path48 = "RaisePigResources/img/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        if (upgraded) {
            this.description = DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0];
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && target instanceof AbstractMonster && !target.isDeadOrEscaped()) {
            // 升级版：造成伤害时触发；未升级版：造成未被格挡的伤害时触发
            if (upgraded || target.currentBlock < damageAmount) {
                this.flash();
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(target, this.owner, new FeedPower((AbstractMonster) target, this.amount))
                );
            }
        }
    }
}
