package RaisePig.card.uncommon;

import RaisePig.Helper.ModHelper;
import RaisePig.actions.FeedAction;
import RaisePig.powers.FeedPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class Overfeeding extends CustomCard {
    public static final String ID = ModHelper.makePath(Overfeeding.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/Strike.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Overfeeding() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 7;
        this.magicNumber = this.baseMagicNumber = 4;
    }
    
    private static final int ENCLOSURE_THRESHOLD = 12;
    private static final int UPGRADED_ENCLOSURE_THRESHOLD = 10;

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 投喂
        AbstractDungeon.actionManager.addToBottom(
                new FeedAction(m, p, this.magicNumber)
        );
        // 造成伤害
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL))
        );
        // 圈养：检查投喂是否达到阈值
        final int threshold = this.upgraded ? UPGRADED_ENCLOSURE_THRESHOLD : ENCLOSURE_THRESHOLD;
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                if (m.hasPower(FeedPower.POWER_ID) && m.getPower(FeedPower.POWER_ID).amount >= threshold) {
                    AbstractDungeon.actionManager.addToTop(
                            new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(m, p, new WeakPower(m, 2, false))
                    );
                }
                this.isDone = true;
            }
        });
    }
}
