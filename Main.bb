Graphics 800,600,0,6
SetBuffer BackBuffer()
AppTitle "MSA 2013 - Physiksimulationen in der Informatik - Jochen Jacobs und Leonard König"

Include "Ph_Main.bb"

Const FPS#=30
Const tick#=1/FPS#

Include "Open.bb"

Local Timer = CreateTimer(FPS)

Repeat
	WaitTimer(Timer)
	
	MainPhysicTick(tick)
	
	Cls
	MainPhysicRender()
	Flip
Until KeyHit(1)

End
;~IDEal Editor Parameters:
;~C#Blitz3D