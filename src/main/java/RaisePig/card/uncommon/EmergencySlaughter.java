package RaisePig.card.uncommon;

import RaisePig.Helper.ModHelper;
import RaisePig.actions.HarvestAction;
import RaisePig.powers.FeedPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class EmergencySlaughter extends CustomCard {
    public static final String ID = ModHelper.makePath(EmergencySlaughter.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/Defend.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public EmergencySlaughter() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 触发收获效果但不消耗投喂
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                if (m.hasPower(FeedPower.POWER_ID)) {
                    int feedAmount = m.getPower(FeedPower.POWER_ID).amount;
                    
                    // 如果到达15层，升级一张卡牌
                    if (feedAmount >= 15) {
                        if (AbstractDungeon.player.masterDeck.hasUpgradableCards()) {
                            AbstractDungeon.gridSelectScreen.open(
                                    AbstractDungeon.player.masterDeck.getUpgradableCards(),
                                    1, "选择一张卡牌升级", true, false, false, false
                            );
                        }
                    }
                    // 如果到达7层但不到15层，增加能量上限
                    else if (feedAmount >= 7) {
                        AbstractDungeon.player.energy.energyMaster++;
                    }
                }
                this.isDone = true;
            }
        });
    }
}
