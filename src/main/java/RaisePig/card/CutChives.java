package RaisePig.card;

import RaisePig.Helper.ModHelper;
import RaisePig.powers.FeedPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class CutChives extends CustomCard {
    public static final String ID = ModHelper.makePath(CutChives.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/CutChives.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public CutChives() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 7;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成伤害
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(
                        m,
                        new DamageInfo(
                                p,
                                this.damage,
                                DamageInfo.DamageType.NORMAL
                        )
                )
        );
        
        // 检查是否斩杀
        AbstractDungeon.actionManager.addToBottom(
                new AbstractGameAction() {
                    @Override
                    public void update() {
                        if ((m.isDying || m.currentHealth <= 0) && !m.halfDead
                                && !m.hasPower("Minion")) {
                            // 转移所有投喂至随机敌人
                            int feedAmount = 0;
                            if (m.hasPower(FeedPower.POWER_ID)) {
                                feedAmount = m.getPower(FeedPower.POWER_ID).amount;
                            }
                            
                            if (feedAmount > 0) {
                                // 找到随机敌人
                                AbstractMonster randomEnemy = AbstractDungeon.getRandomMonster();
                                if (randomEnemy != null && !randomEnemy.isDeadOrEscaped()) {
                                    AbstractDungeon.actionManager.addToBottom(
                                            new ApplyPowerAction(
                                                    randomEnemy,
                                                    p,
                                                    new FeedPower(randomEnemy, feedAmount)
                                            )
                                    );
                                }
                            }
                        }
                        this.isDone = true;
                    }
                }
        );
    }
}
