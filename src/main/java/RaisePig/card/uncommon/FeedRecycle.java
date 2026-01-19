package RaisePig.card.uncommon;

import RaisePig.Helper.ModHelper;
import RaisePig.powers.FeedPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class FeedRecycle extends CustomCard {
    public static final String ID = ModHelper.makePath(FeedRecycle.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/Strike.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public FeedRecycle() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 3;
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
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                if (m.hasPower(FeedPower.POWER_ID)) {
                    int feedAmount = m.getPower(FeedPower.POWER_ID).amount;
                    int damageAmount = feedAmount * magicNumber;
                    int reduceAmount = feedAmount / 2;
                    
                    // 造成伤害
                    AbstractDungeon.actionManager.addToTop(
                            new DamageAction(m, new DamageInfo(p, damageAmount, DamageInfo.DamageType.NORMAL))
                    );
                    // 移除一半投喂
                    if (reduceAmount > 0) {
                        AbstractDungeon.actionManager.addToTop(
                                new ReducePowerAction(m, p, FeedPower.POWER_ID, reduceAmount)
                        );
                    }
                }
                this.isDone = true;
            }
        });
    }
}
