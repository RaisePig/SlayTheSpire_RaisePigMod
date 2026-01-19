package RaisePig.powers;

import RaisePig.Helper.ModHelper;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PiganomicsPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("PiganomicsPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PiganomicsPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;

        String path128 = "RaisePigResources/img/powers/Example84.png";
        String path48 = "RaisePigResources/img/powers/Example32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    // 当使用收获牌时调用
    public void onHarvestUsed() {
        this.flash();
        AbstractDungeon.player.gainGold(this.amount);
    }

    @Override
    public void onVictory() {
        int extraEnergy = AbstractDungeon.player.energy.energyMaster - 3;
        if (extraEnergy > 0) {
            int gold = extraEnergy * 5;
            AbstractDungeon.player.gainGold(gold);
        }
    }
}
