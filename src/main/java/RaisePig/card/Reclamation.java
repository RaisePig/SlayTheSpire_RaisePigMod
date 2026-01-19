package RaisePig.card;

import RaisePig.Helper.ModHelper;
import RaisePig.powers.RaisePigPower;
import basemod.abstracts.CustomCard;
import basemod.devcommands.power.Power;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class Reclamation extends CustomCard {
    public static final String ID = ModHelper.makePath(Reclamation.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/Reclamation.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Reclamation() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 8;
        this.magicNumber = this.baseMagicNumber = 2;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
            this.upgradeMagicNumber(-1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 失去M层养猪
        if (p.hasPower(RaisePigPower.POWER_ID)) {
            int raisePig = p.getPower(RaisePigPower.POWER_ID).amount;
            if (raisePig > this.magicNumber){
                AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(
                            p,
                            p,
                            new RaisePigPower(p, -this.magicNumber)
                    )
                );
            }
            else {
                AbstractDungeon.actionManager.addToBottom(
                        new RemoveSpecificPowerAction(
                                p,
                                p,
                                RaisePigPower.POWER_ID
                        )
                );
            }
        }

        // 根据养猪层数概率对敌人造成伤害，否则对自己造成伤害
        if (p.hasPower(RaisePigPower.POWER_ID)) {
            int raisePigAmount = p.getPower(RaisePigPower.POWER_ID).amount;
            int random = AbstractDungeon.cardRandomRng.random(1, 10);
            if (random <= raisePigAmount) {
                // 对敌人造成伤害
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
            } else {
                // 对自己造成伤害
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(
                                p,
                                new DamageInfo(
                                        p,
                                        this.damage,
                                        DamageInfo.DamageType.NORMAL
                                )
                        )
                );
            }
        } else {
            // 没有养猪状态，对自己造成伤害
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(
                            p,
                            new DamageInfo(
                                    p,
                                    this.damage,
                                    DamageInfo.DamageType.NORMAL
                            )
                    )
            );
        }
    }
}
