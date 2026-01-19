package RaisePig.card;

import RaisePig.Helper.ModHelper;
import RaisePig.actions.IncreaseMaxEnergyAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class FinancialManagement extends CustomCard {
    public static final String ID = ModHelper.makePath(FinancialManagement.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/FinancialManagement.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public FinancialManagement() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 12;
        this.magicNumber = this.baseMagicNumber = 2;
        this.isMultiDamage = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(6);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 失去M点能量上限
        AbstractDungeon.actionManager.addToBottom(
                new IncreaseMaxEnergyAction(-this.magicNumber)
        );
        
        // 对所有敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(
                        p,
                        this.multiDamage,
                        DamageInfo.DamageType.NORMAL,
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
                )
        );
        
        // 检查是否有敌人被斩杀
        AbstractDungeon.actionManager.addToBottom(
                new AbstractGameAction() {
                    @Override
                    public void update() {
                        boolean killed = false;
                        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                            if ((mo.isDying || mo.currentHealth <= 0) && !mo.halfDead
                                    && !mo.hasPower("Minion")) {
                                killed = true;
                                break;
                            }
                        }
                        if (killed) {
                            // 能量变为3点
                            EnergyPanel.totalCount = 3;
                        }
                        this.isDone = true;
                    }
                }
        );
    }
}
