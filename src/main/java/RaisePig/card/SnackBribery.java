package RaisePig.card;

import RaisePig.Helper.ModHelper;
import RaisePig.powers.FeedPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class SnackBribery extends CustomCard {
    public static final String ID = ModHelper.makePath(SnackBribery.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/SnackBribery.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public SnackBribery() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 投喂1
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        m,
                        p,
                        new FeedPower(m, 1)
                )
        );
        // 其本回合力量-99
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                        m,
                        p,
                        new StrengthPower(m, -99),
                        -99,
                        true,
                        AbstractGameAction.AttackEffect.NONE
                )
        );
        if (!m.hasPower("Artifact")) {
                addToBot(
                        new ApplyPowerAction(
                                m,
                                p,
                                new GainStrengthPower(m, 99),
                                99,
                                true,
                                AbstractGameAction.AttackEffect.NONE)
                );
        }
    }
}
