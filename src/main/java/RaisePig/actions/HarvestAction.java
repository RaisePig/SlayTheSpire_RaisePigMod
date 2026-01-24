package RaisePig.actions;

import RaisePig.Helper.ModHelper;
import RaisePig.powers.FeedPower;
import RaisePig.powers.LoseMaxEnergyPower;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

//action不需要注册，在卡牌的use方法中直接new即可。
public class HarvestAction extends AbstractGameAction {
    private boolean openedScreen = false;
    private boolean removedFeed = false;
    private int feedAmount = 0;

    public HarvestAction(AbstractCreature target, AbstractCreature source) {
        this.target = target;
        this.source = source;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        // ==== 1) 结算惩罚状态（挂在玩家身上） ====
        if (AbstractDungeon.player != null) {
            // LoseMaxEnergy：失去一半能量上限（每次触发减少1层）
            if (AbstractDungeon.player.hasPower("RaisePig:LoseMaxEnergyPower")) {
                AbstractDungeon.player.energy.energy /= 2;
                addToTop(
                        new ApplyPowerAction(source, source, new LoseMaxEnergyPower(source, -1), -1));
                if (source.getPower("RaisePig:LoseMaxEnergyPower").amount==0) {
                    addToBot(
                        new RemoveSpecificPowerAction(source, source, "RaisePig:LoseMaxEnergyPower")
                    );
                }
            }

            // LoseHP：失去对应层数生命（一次性）
            if (AbstractDungeon.player.hasPower("RaisePig:LoseHPPower")) {
                int loss = AbstractDungeon.player.getPower("RaisePig:LoseHPPower").amount;
                if (loss > 0) {
                    AbstractDungeon.actionManager.addToTop(
                            new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, loss));
                }
                AbstractDungeon.actionManager.addToTop(
                        new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                                "RaisePig:LoseHPPower"));
            }
        }

        // 如果目标身上有投喂状态
        if (target.hasPower(ModHelper.makePath("FeedPower"))) {
            // 记录投喂状态层数
            feedAmount = target.getPower(ModHelper.makePath("FeedPower")).amount;

            // 如果到达15层投喂，打开卡牌选择界面（优先处理15层效果）
            if (feedAmount >= 15 && !openedScreen) {
                // 检查是否有可升级的卡牌
                if (AbstractDungeon.player.masterDeck.hasUpgradableCards()) {
                    // 打开卡牌选择界面，选择一张可升级的卡牌
                    AbstractDungeon.gridSelectScreen.open(
                            AbstractDungeon.player.masterDeck.getUpgradableCards(),
                            1,
                            "选择一张卡牌升级",
                            true,
                            false,
                            false,
                            false);
                    openedScreen = true;
                    // 不立即完成，等待玩家选择
                    return;
                }
            }

            // 如果未达到15层但达到7层，永久增加1点能量上限
            if (feedAmount >= 7 && feedAmount < 15) {
                // 永久增加能量上限
                AbstractDungeon.player.energy.energyMaster++;
                // 通知RaisePigBook记录这个增加
                if (AbstractDungeon.player.hasRelic(ModHelper.makePath("RaisePigBook"))) {
                    RaisePig.relics.RaisePigBook book = (RaisePig.relics.RaisePigBook) AbstractDungeon.player
                            .getRelic(ModHelper.makePath("RaisePigBook"));
                    if (book != null) {
                        book.onEnergyMasterIncrease();
                    }
                }
            }

            // 如果已经打开了选择界面，检查是否已经选择了卡牌
            if (openedScreen && !AbstractDungeon.isScreenUp) {
                // 检查是否有选中的卡牌
                if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()
                        && AbstractDungeon.gridSelectScreen.forUpgrade) {
                    // 升级选中的卡牌
                    for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                        c.upgrade();
                        AbstractDungeon.player.bottledCardUpgradeCheck(c);
                        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                        AbstractDungeon.topLevelEffects
                                .add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                    }
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                }
                // 无论是否选择了卡牌，都标记为已完成选择
                openedScreen = false;
            }

            // 如果已经打开了选择界面但还在等待选择，不完成Action
            if (openedScreen && AbstractDungeon.isScreenUp) {
                return;
            }

            // 消耗所有投喂（移除）- 只执行一次
            if (!removedFeed) {
                target.getPower(ModHelper.makePath("FeedPower")).flash();
                AbstractDungeon.actionManager.addToBottom(
                        new RemoveSpecificPowerAction(
                                target,
                                source,
                                FeedPower.POWER_ID));
                removedFeed = true;
            }
        }

        this.isDone = true;// isDone=true是必须写的，这表示你这个action执行完了。
    }
}
