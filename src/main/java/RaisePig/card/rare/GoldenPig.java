package RaisePig.card.rare;

import RaisePig.Helper.ModHelper;
import RaisePig.powers.RaisePigPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

/**
 * 金猪献瑞 - RARE 技能牌
 * 费用: 1
 * 效果: 获得 3 层养猪。获得 25 金币。消耗。
 * 升级: 获得 4 层养猪，35 金币
 * 理念: 稀有的财富卡，消耗型一次性暴富+发育
 */
public class GoldenPig extends CustomCard {
    public static final String ID = ModHelper.makePath(GoldenPig.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/Defend.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public GoldenPig() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 3;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获得养猪层数
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new RaisePigPower(p, this.magicNumber))
        );
        // 获得金币
        int gold = this.upgraded ? 35 : 25;
        AbstractDungeon.actionManager.addToBottom(new GainGoldAction(gold));
    }
}
