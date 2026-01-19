package RaisePig.card;

import RaisePig.Helper.ModHelper;
import RaisePig.actions.IsNeedRaisePigAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAndEnableControlsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;
import static com.megacrit.cardcrawl.ui.panels.EnergyPanel.totalCount;

public class BuildupDefend extends CustomCard {
    public static final String ID = ModHelper.makePath(BuildupDefend.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/BuildupDefend.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;

    public BuildupDefend() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;
        //添加标签让变化卡牌时，例如变换所有打击和防御卡，能够识别这些卡牌
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeMagicNumber(1); // 将该卡牌的回复能量数提高1点。
//             加上以下两行就能使用UPGRADE_DESCRIPTION了（如果你写了的话）
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    /**
     * 当卡牌被使用时，调用这个方法。
     *
     * @param p 你的玩家实体类。
     * @param m 指向的怪物类。（无指向时为null，包括攻击所有敌人时）
     */
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int rate = this.upgraded ? 3 : 2;
        // 获得1点能量
        AbstractDungeon.actionManager.addToBottom(
                new GainEnergyAction(magicNumber)
        );
        // 判定养猪能力是否触发
        AbstractDungeon.actionManager.addToBottom(
                new IsNeedRaisePigAction(
                        p
                )
        );
        // 获得双倍防御
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(
                        p,
                        totalCount * rate
                )
        );
    }
}