package RaisePig.card.rare;

import RaisePig.Helper.ModHelper;
import RaisePig.powers.FeedPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

/**
 * 终极丰收 - RARE 攻击牌
 * 费用: 3
 * 效果: 对所有敌人造成伤害，伤害值等于所有敌人投喂层数之和的 3 倍。清除所有投喂。消耗。
 * 升级: 倍率 3→4
 * 理念: AOE终结技，投喂总量的最终爆发
 */
public class FinalHarvest extends CustomCard {
    public static final String ID = ModHelper.makePath(FinalHarvest.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/Strike.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    public FinalHarvest() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 3;
        this.isMultiDamage = true;
        this.exhaust = true;
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
        // 计算所有敌人的投喂总和
        int totalFeed = 0;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped() && mo.hasPower(FeedPower.POWER_ID)) {
                totalFeed += mo.getPower(FeedPower.POWER_ID).amount;
            }
        }
        
        int damage = totalFeed * this.magicNumber;
        
        // 造成AOE伤害
        this.baseDamage = damage;
        this.calculateCardDamage(null);
        
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
                        AbstractGameAction.AttackEffect.SLASH_HEAVY)
        );
        
        // 清除所有敌人的投喂
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped() && mo.hasPower(FeedPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(
                        new RemoveSpecificPowerAction(mo, p, FeedPower.POWER_ID)
                );
            }
        }
    }
}
