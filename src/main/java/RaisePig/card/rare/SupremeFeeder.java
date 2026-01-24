package RaisePig.card.rare;

import RaisePig.Helper.ModHelper;
import RaisePig.actions.FeedAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

/**
 * 饲料霸主 - RARE 技能牌
 * 费用: 2
 * 效果: 对所有敌人投喂 5。获得 15 点格挡。
 * 升级: 投喂 5→7，格挡 15→20
 * 理念: 强力AOE投喂+防御，攻防兼备
 */
public class SupremeFeeder extends CustomCard {
    public static final String ID = ModHelper.makePath(SupremeFeeder.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/Defend.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public SupremeFeeder() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 5;
        this.block = this.baseBlock = 15;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
            this.upgradeBlock(5);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对所有敌人投喂
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped()) {
                AbstractDungeon.actionManager.addToBottom(
                        new FeedAction(mo, p, this.magicNumber)
                );
            }
        }
        
        // 获得格挡
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    }
}
