package RaisePig.card.rare;

import RaisePig.Helper.ModHelper;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

/**
 * 猪队友 - RARE 技能牌
 * 费用: 1
 * 效果: 获得 8 点格挡。每有 1 点能量上限超过 3，额外获得 3 点格挡、抽 1 张牌、获得 1 点能量。
 * 升级: 基础格挡 8→12
 * 理念: 发育后的超级工具牌，能量上限越高收益越大
 */
public class PiggyBackup extends CustomCard {
    public static final String ID = ModHelper.makePath(PiggyBackup.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/Defend.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public PiggyBackup() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = 8;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(4);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int extraEnergy = Math.max(0, p.energy.energyMaster - 3);
        
        // 基础格挡 + 额外格挡
        int totalBlock = this.block + extraEnergy * 3;
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, totalBlock));
        
        // 额外抽牌
        if (extraEnergy > 0) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, extraEnergy));
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(extraEnergy));
        }
    }
}
