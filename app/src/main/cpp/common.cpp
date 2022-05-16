#include <sstream>

#include "common.hpp"
#include "ImageConverter.hpp"

lpin::opencv::GetState_StateCode state = lpin::opencv::GetState_StateCode::Not_Defined;
cv::Mat img_base;
cv::Mat img_query;

int count_trials;

char buffer_status[256];
char buffer_log[256];
char buffer_fullResult[128 * 10001];

int length_status;
int length_log;
int length_fullResult;

int lpin::opencv::GetState()
{
	return state;
}

char *lpin::opencv::GetPtrOfString(int requestCode)
{
	switch ( requestCode )
	{
		case lpin::opencv::GetPtrOfString_RequestCode::Gimme_Status:
			return buffer_status;
		case lpin::opencv::GetPtrOfString_RequestCode::Gimme_LogLine:
			return buffer_log;
		case lpin::opencv::GetPtrOfString_RequestCode::Gimme_FullResult:
			return buffer_fullResult;
		default:
			return 0;
	}
}

int lpin::opencv::GetLengthOfString(int requestCode)
{
	switch ( requestCode )
	{
		case lpin::opencv::GetPtrOfString_RequestCode::Gimme_Status:
			return length_status;
		case lpin::opencv::GetPtrOfString_RequestCode::Gimme_LogLine:
			return length_log;
		case lpin::opencv::GetPtrOfString_RequestCode::Gimme_FullResult:
			return length_fullResult;
		default:
			return 0;
	}
}

int lpin::opencv::Initialize(int taskCode)
{
	if ( taskCode != 0 || state != Not_Defined )
	{
		length_status = std::sprintf(buffer_status, "Trial#%.3d: Initialize() failed.", count_trials);
		return -1;
	}

	state = WaitFor_BaseImage;
	length_status = std::sprintf(buffer_status, "Trial#%.3d: Initialize() success. Waiting for Base Image...", count_trials);
	length_fullResult = std::sprintf(buffer_fullResult, "Trial#,Calculated distance,Actual distance,Difference,Result\r\n");

	return 0;
}

int lpin::opencv::PutImage(void *bitmap, int width, int height)
{
	if ( state != WaitFor_BaseImage && state != WaitFor_QueryImage )
	{
		length_status = std::sprintf(buffer_status, "Trial#%.3d: PutImage() failed.", count_trials);
		return -1;
	}

	if ( state == WaitFor_BaseImage )
	{
		length_status = std::sprintf(buffer_status, "Trial#%.3d: PutImage() success. Waiting for Query Image...", count_trials);
		state = WaitFor_QueryImage;
	}
	else
	{
		length_status = std::sprintf(buffer_status, "Trial#%.3d: PutImage() success. Waiting for Byte Block...", count_trials);
		state = WaitFor_ByteBlock;
	}

	return 0;
}

int lpin::opencv::PutByteBlock(char *data, int length)
{
	if ( state != WaitFor_ByteBlock )
	{
		length_status = std::sprintf(buffer_status, "Trial#%.3d: PutByteBlock() failed.", count_trials);
		return -1;
	}

	length_status = std::sprintf(buffer_status, "Trial#%.3d: PutByteBlock() success. Ready to run Process().", count_trials);
	state = Ready_ToStartTrial;

	return 0;
}

int lpin::opencv::Process()
{
	if ( state != Ready_ToStartTrial )
	{
		length_status = std::sprintf(buffer_status, "Trial#%.3d: Process() failed.", count_trials);
		return -1;
	}

	length_log = std::sprintf(buffer_log, "Trial#%.3d | Calculated: %.2fm, Actual: %.2fm, Diff: %.2fm | Result: PASS", count_trials, 12.34, 12.34, 0.00);
	length_fullResult += std::sprintf(buffer_fullResult + length_fullResult, "%d,%.8f,%.8f,%.8f,1\r\n", count_trials, 12.34567891, 12.34567891, 0.0);


	if ( count_trials < 100 )
	{
		length_status = std::sprintf(buffer_status, "Trial#%.3d: Process() success. Waiting for Next Base Image...", count_trials);
		state = WaitFor_BaseImage;

	}
	else
	{
		length_status = std::sprintf(buffer_status, "Trial#%.3d: Process() success. Test complete.", count_trials);
		state = Completed;
	}

	++count_trials;

	return 0;
}
