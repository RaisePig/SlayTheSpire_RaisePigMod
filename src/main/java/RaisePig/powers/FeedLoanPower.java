package RaisePig.powers;

import RaisePig.Helper.ModHelper;
import RaisePig.actions.IncreaseMaxEnergyAction;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FeedLoanPower extends AbstractPower {
    // 能力的ID
    public static final String POWER_ID = ModHelper.makePath("FeedLoanPower");
    // 能力的本地化字段
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // 能力的名称
    private static final String NAME = powerStrings.NAME;
    // 能力的描述
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    public int energyLoss; // 失去的能量上限（总消耗）
    private boolean triggered = false; // 是否已触发

    public FeedLoanPower(AbstractCreature owner, int amount, int energyLoss) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        this.amount = amount; // 层数
        this.energyLoss = energyLoss; // 失去的能量上限（总消耗）
        this.triggered = false;

        // 添加一大一小两张能力图（使用Feed图片，如果没有FeedLoan图片的话）
        String path128 = "RaisePigResources/img/powers/Feed84.png";
        String path48 = "RaisePigResources/img/powers/Feed32.png";
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        // 首次添加能力更新描述
        this.updateDescription();
    }

    // 当打出卡牌时触发
    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        // 检查是否是收获牌
        if (isHarvestCard(card) && !triggered) {
            this.flash();
            triggered = true;
            
            // 失去能量上限
            AbstractDungeon.actionManager.addToBottom(
                    new IncreaseMaxEnergyAction(-energyLoss)
            );
            
            // 失去9点生命
            AbstractDungeon.actionManager.addToBottom(
                    new LoseHPAction(owner, owner, 9)
            );
            
            // 移除这个Power（只触发一次）
            AbstractDungeon.actionManager.addToBottom(
                    new com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction(
                            owner,
                            owner,
                            POWER_ID
                    )
            );
        }
    }

    // 检查是否是收获牌
    private boolean isHarvestCard(AbstractCard card) {
        // 检查卡牌描述中是否包含"收获"关键词
        String description = card.rawDescription;
        if (description != null && (description.contains(" raisepig:收获 。"))) {
            return true;
        }
        return false;
    }

    // 能力在更新时如何修改描述
    @Override
    public void updateDescription() {
        if (triggered) {
            this.description = "已触发。";
        } else {
            // 显示总能量损失，如果层数大于1也显示层数
            if (this.amount > 1) {
                this.description = String.format("下次打出 #y收获 牌时，失去 %d 点 [E] 上限和9点生命。（层数：%d）", energyLoss, this.amount);
            } else {
                this.description = String.format(DESCRIPTIONS[0], energyLoss);
            }
        }
    }
}
