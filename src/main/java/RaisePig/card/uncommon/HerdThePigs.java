package RaisePig.card.uncommon;

import RaisePig.Helper.ModHelper;
import RaisePig.actions.FeedAction;
import RaisePig.powers.FeedPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

import static RaisePig.characters.MyCharacter.PlayerColorEnum.RaisePigPink;

public class HerdThePigs extends CustomCard {
    public static final String ID = ModHelper.makePath(HerdThePigs.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "RaisePigResources/img/cards/Defend.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = RaisePigPink;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public HerdThePigs() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 1;
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
        // 转移投喂
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                if (m.hasPower(FeedPower.POWER_ID)) {
                    int feedAmount = m.getPower(FeedPower.POWER_ID).amount;
                    
                    // 找到另一个敌人
                    ArrayList<AbstractMonster> otherMonsters = new ArrayList<>();
                    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        if (!mo.isDeadOrEscaped() && mo != m) {
                            otherMonsters.add(mo);
                        }
                    }
                    
                    if (!otherMonsters.isEmpty()) {
                        AbstractMonster target = otherMonsters.get(AbstractDungeon.cardRandomRng.random(otherMonsters.size() - 1));
                        AbstractDungeon.actionManager.addToTop(
                                new FeedAction(target, p, feedAmount)
                        );
                        AbstractDungeon.actionManager.addToTop(
                                new RemoveSpecificPowerAction(m, p, FeedPower.POWER_ID)
                        );
                    }
                }
                this.isDone = true;
            }
        });
        
        // 抽牌
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
    }
}
