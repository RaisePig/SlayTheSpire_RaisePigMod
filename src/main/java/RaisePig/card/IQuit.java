package RaisePig.card;

import RaisePig.Helper.ModHelper;
import RaisePig.actions.KillPigAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class IQuit extends CustomCard {
    public static final String ID = ModHelper.makePath(IQuit.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/IQuit.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public IQuit() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void upgrade() {
        this.upgradeName();
        this.upgradeBaseCost(2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对敌人执行收菜
        if (!m.isDeadOrEscaped()) {
            AbstractDungeon.actionManager.addToBottom(
                    new KillPigAction(m)
            );
        }
    }
}
