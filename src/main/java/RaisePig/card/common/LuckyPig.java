package RaisePig.card.common;

import RaisePig.Helper.ModHelper;
import RaisePig.powers.RaisePigPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class LuckyPig extends CustomCard {
    public static final String ID = ModHelper.makePath(LuckyPig.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/LuckyPig.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.POWER;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public LuckyPig() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
        this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new RaisePigPower(p, this.magicNumber)));
        // cards.json 升级描述：恢复 raisepig:养猪 层数的生命
        if (this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.AbstractGameAction() {
                @Override
                public void update() {
                    int healAmt = 0;
                    if (p.hasPower(RaisePigPower.POWER_ID)) {
                        healAmt = p.getPower(RaisePigPower.POWER_ID).amount;
                    }
                    if (healAmt > 0) {
                        AbstractDungeon.actionManager.addToTop(
                                new com.megacrit.cardcrawl.actions.common.HealAction(p, p, healAmt)
                        );
                    }
                    this.isDone = true;
                }
            });
        }
    }
}
