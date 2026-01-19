package RaisePig.card;

import RaisePig.Helper.ModHelper;
import RaisePig.actions.HarvestAction;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class AutumnHarvestUprising extends CustomCard {
    public static final String ID = ModHelper.makePath(AutumnHarvestUprising.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/AutumnHarvestUprising.png";
    private static final int COST = 4;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public AutumnHarvestUprising() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        this.upgradeName();
        this.upgradeBaseCost(3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对敌人执行收获
        addToBot(
                new HarvestAction(m, p)
        );
    }
}
