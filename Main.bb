Graphics 800,600,0,6
SetBuffer BackBuffer()
AppTitle "MSA 2013 - Physiksimulationen in der Informatik - Copyright © 2013 by Jochen Jacobs & Leonard Robert Koenig"

Include "Ph_Main.bb"

Const FPS#=30
Const tick#=1/FPS#

Ph_ReadFromFile("BallOnSquare.phS")

Local pos1#[1]
Local force2#[1]
Local pos2#[1]

Local LastTime# = MilliSecs()
Local Timer = CreateTimer(FPS)
Local TimerError

Global CollisonPointTemp#[1]

Repeat
	WaitTimer(Timer)
	
	Local Obj.Ph_Object = After ( First Ph_Object )
	force2[0] = 0
	force2[1] = 5
	
;	Ph_ApplyForce(Obj, force2, pos2, False)
	
;	DebugLog Obj\Vel[0] + ", " + Obj\Vel[1]
	
	MainPhysicTick(tick)
	LastTime=MilliSecs()
	Cls
	MainPhysicRender()
	
	Plot CollisonPointTemp[0]*100, CollisonPointTemp[1]*100
	
	Flip
Until KeyHit(1)

End
;~IDEal Editor Parameters:
;~C#Blitz3D