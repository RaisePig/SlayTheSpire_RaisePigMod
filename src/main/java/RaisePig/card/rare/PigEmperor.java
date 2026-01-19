package RaisePig.card.rare;

import RaisePig.Helper.ModHelper;
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

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

/**
 * 猪帝降临 - RARE 攻击牌
 * 费用: 3
 * 效果: 造成 30 点伤害。若目标投喂≥15层，伤害翻倍。
 * 升级: 伤害 30→40
 * 理念: 极限投喂的终极奖励，鼓励堆叠到15层后一击必杀
 */
public class PigEmperor extends CustomCard {
    public static final String ID = ModHelper.makePath(PigEmperor.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/Strike.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public PigEmperor() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 30;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(10);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int finalDamage = this.damage;
        
        // 检查投喂层数
        if (m.hasPower(FeedPower.POWER_ID) && m.getPower(FeedPower.POWER_ID).amount >= 15) {
            finalDamage *= 2;
        }
        
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, finalDamage, DamageInfo.DamageType.NORMAL),
                        AbstractGameAction.AttackEffect.SLASH_HEAVY)
        );
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();
        // 更新描述显示实际伤害
    }
}
