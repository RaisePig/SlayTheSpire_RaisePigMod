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
 * 猪吃撑了 - RARE 攻击牌
 * 费用: 1
 * 效果: 造成 5 点伤害。若目标投喂≥7层，造成 3 倍伤害。若≥15层，改为 5 倍伤害。
 * 升级: 伤害 5→7
 * 理念: 投喂阶梯式爆发，里程碑奖励
 */
public class PiggedOut extends CustomCard {
    public static final String ID = ModHelper.makePath(PiggedOut.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/Strike.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public PiggedOut() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.damage = this.baseDamage = 5;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int finalDamage = this.damage;
        
        if (m.hasPower(FeedPower.POWER_ID)) {
            int feedAmount = m.getPower(FeedPower.POWER_ID).amount;
            if (feedAmount >= 15) {
                finalDamage *= 5;
            } else if (feedAmount >= 7) {
                finalDamage *= 3;
            }
        }
        
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, finalDamage, DamageInfo.DamageType.NORMAL),
                        AbstractGameAction.AttackEffect.SMASH)
        );
    }
}
