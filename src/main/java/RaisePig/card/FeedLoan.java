package RaisePig.card;

import RaisePig.Helper.ModHelper;
import RaisePig.actions.ApplyFeedLoanPowerAction;
import RaisePig.powers.FeedPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class FeedLoan extends CustomCard {
    public static final String ID = ModHelper.makePath(FeedLoan.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/FeedLoan.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public FeedLoan() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 投喂*2
        if (!m.isDeadOrEscaped()) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(
                            m,
                            p,
                            new FeedPower(m, 2)
                    )
            );
        }
        // 在本场战斗中，你下次收获时，失去M点能量上限和9点生命
        // 使用自定义Action来正确叠加Power
        AbstractDungeon.actionManager.addToBottom(
                new ApplyFeedLoanPowerAction(p, 1, this.magicNumber)
        );
    }
}
