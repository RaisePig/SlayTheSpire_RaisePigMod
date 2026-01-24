package RaisePig.card.common;

import RaisePig.Helper.ModHelper;
import RaisePig.powers.FeedPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class FortifySty extends CustomCard {
    public static final String ID = ModHelper.makePath(FortifySty.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/FortifySty.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final int ENCLOSURE_THRESHOLD = 7;
    private static final int UPGRADED_ENCLOSURE_THRESHOLD = 5;

    public FortifySty() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.block = this.baseBlock = 9;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(4);
        }
        this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 基础格挡
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

        // cards.json：圈养 总数X（按全场投喂总和判定），满足则额外获得 !B! 点格挡
        final int threshold = this.upgraded ? UPGRADED_ENCLOSURE_THRESHOLD : ENCLOSURE_THRESHOLD;
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                int totalFeed = 0;
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!mo.isDeadOrEscaped() && mo.hasPower(FeedPower.POWER_ID)) {
                        totalFeed += mo.getPower(FeedPower.POWER_ID).amount;
                    }
                }
                if (totalFeed >= threshold) {
                    AbstractDungeon.actionManager.addToTop(
                            new GainBlockAction(p, p, block));
                }
                this.isDone = true;
            }
        });
    }
}
