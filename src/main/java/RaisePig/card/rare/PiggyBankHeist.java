package RaisePig.card.rare;

import RaisePig.Helper.ModHelper;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

/**
 * 猪仔银行劫案 - RARE 技能牌
 * 费用: 0
 * 效果: 失去 2 点能量上限。抽 4 张牌。获得 2 点能量。消耗。
 * 升级: 抽 5 张牌，获得 3 点能量
 * 理念: 极端爆发牌，牺牲发育换取一回合大量资源
 */
public class PiggyBankHeist extends CustomCard {
    public static final String ID = ModHelper.makePath(PiggyBankHeist.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/Defend.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    public PiggyBankHeist() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 4;
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
        // 失去能量上限
        if (p.energy.energyMaster > 0) {
            p.energy.energyMaster = Math.max(0, p.energy.energyMaster - 2);
        }
        
        // 抽牌
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
        
        // 获得能量
        int energy = this.upgraded ? 3 : 2;
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(energy));
    }
}
