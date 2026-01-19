package RaisePig.card;

import RaisePig.Helper.ModHelper;
import RaisePig.powers.FeedPower;
import RaisePig.powers.RaisePigPower;
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

public class FatPigArchway extends CustomCard {
    public static final String ID = ModHelper.makePath(FatPigArchway.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/FatPigArchway.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int MAX_RAISE_PIG_FROM_CARD = 4;

    public FatPigArchway() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 8;
    }

    @Override
    public void upgrade() {
        this.upgradeName();
        this.upgradeDamage(3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 记录伤害前的生命值
        int healthBefore = m.currentHealth;
        
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
        
        // 检查是否成功造成伤害（使用自定义Action来延迟检查）
        AbstractDungeon.actionManager.addToBottom(
                new AbstractGameAction() {
                    @Override
                    public void update() {
                        // 检查是否成功造成伤害（生命值减少或死亡）
                        boolean damageDealt = m.currentHealth < healthBefore || m.isDying || m.isDead;
                        
                        if (damageDealt) {
                            // 成功造成伤害，获得1层养猪（但不超过上限）
                            if (p.hasPower(RaisePigPower.POWER_ID)) {
                                int currentRaisePig = p.getPower(RaisePigPower.POWER_ID).amount;
                                if (currentRaisePig < MAX_RAISE_PIG_FROM_CARD) {
                                    AbstractDungeon.actionManager.addToBottom(
                                            new ApplyPowerAction(
                                                    p,
                                                    p,
                                                    new RaisePigPower(p, 1)
                                            )
                                    );
                                }
                            } else {
                                AbstractDungeon.actionManager.addToBottom(
                                        new ApplyPowerAction(
                                                p,
                                                p,
                                                new RaisePigPower(p, 1)
                                        )
                                );
                            }
                        } else {
                            // 否则投喂*1
                            AbstractDungeon.actionManager.addToBottom(
                                    new ApplyPowerAction(
                                            m,
                                            p,
                                            new FeedPower(m, 1)
                                    )
                            );
                        }
                        this.isDone = true;
                    }
                }
        );
    }
}
