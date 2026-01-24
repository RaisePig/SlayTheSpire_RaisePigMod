package RaisePig.card.common;

import RaisePig.Helper.ModHelper;
import RaisePig.actions.FeedAction;
import RaisePig.powers.FeedPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class TreatBribery extends CustomCard {
    public static final String ID = ModHelper.makePath(TreatBribery.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/TreatBribery.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public TreatBribery() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 4;
        this.baseBlock = this.block = 5;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(1);
            this.upgradeMagicNumber(2);
            this.upgradeBlock(-2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // cards.json:
        // - 投喂 2
        // - 目标本回合 力量 -!M!
        // - 圈养 !B! ：额外 力量 -5
        AbstractDungeon.actionManager.addToBottom(
                new FeedAction(m, p, 2)
        );

        // 本回合临时减力量：Strength -X + GainStrength X（无人工制品时）
        int strengthLoss = this.magicNumber;
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p, new StrengthPower(m, -strengthLoss), -strengthLoss, true, AbstractGameAction.AttackEffect.NONE)
        );
        if (!m.hasPower("Artifact")) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(m, p, new GainStrengthPower(m, strengthLoss), strengthLoss, true, AbstractGameAction.AttackEffect.NONE)
            );
        }

        // 圈养：阈值使用 !B!（本卡 block 字段），满足则额外临时 -5 力量
        final int enclosureThreshold = this.block;
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                if (m != null && !m.isDeadOrEscaped()
                        && m.hasPower(FeedPower.POWER_ID)
                        && m.getPower(FeedPower.POWER_ID).amount >= enclosureThreshold) {
                    AbstractDungeon.actionManager.addToTop(
                            new ApplyPowerAction(m, p, new StrengthPower(m, -5), -5, true, AbstractGameAction.AttackEffect.NONE)
                    );
                    if (!m.hasPower("Artifact")) {
                        AbstractDungeon.actionManager.addToTop(
                                new ApplyPowerAction(m, p, new GainStrengthPower(m, 5), 5, true, AbstractGameAction.AttackEffect.NONE)
                        );
                    }
                }
                this.isDone = true;
            }
        });
    }
}
