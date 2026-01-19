package RaisePig.card;

import RaisePig.Helper.ModHelper;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class DevelopIsImportant extends CustomCard {
    public static final String ID = ModHelper.makePath(DevelopIsImportant.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/DevelopIsImportant.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public DevelopIsImportant() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 5;
        this.magicNumber = this.baseMagicNumber = 2;
        this.isMultiDamage = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(1);
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        // 只有能量上限不小于10时才能打出
        if (p.energy.energy < 10) {
            this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return super.canUse(p, m);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 消耗一半能量上限的能量
        int energyToLose = p.energy.energy / 2;
        int currentEnergy = EnergyPanel.totalCount;
        
        if (currentEnergy >= energyToLose) {
            // 足以消耗
            AbstractDungeon.actionManager.addToBottom(
                    new LoseEnergyAction(energyToLose)
            );
            
            // 对所有敌人造成M次伤害
            for (int i = 0; i < this.magicNumber; i++) {
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAllEnemiesAction(
                                p,
                                this.damage,
                                DamageInfo.DamageType.NORMAL,
                                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
                        )
                );
            }
        } else {
            // 对所有敌人造成M次伤害
            for (int i = 0; i < this.magicNumber; i++) {
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAllEnemiesAction(
                                p,
                                this.damage,
                                DamageInfo.DamageType.NORMAL,
                                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
                        )
                );
            }
            // 不足以消耗，额外减少20点生命上限
            AbstractDungeon.actionManager.addToBottom(
                    new AbstractGameAction() {
                        @Override
                        public void update() {
                            p.decreaseMaxHealth(20);
                            this.isDone = true;
                        }
                    }
            );
        }
    }
}
